package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import pmtischlerLib.Pid;

@Autonomous(name="Base Auto", group="Pushbot")
@Disabled
public class BaseAuto extends LinearOpMode {

    /* Declare OpMode members. */
    OneTwoFiveBot robot   = new OneTwoFiveBot();   // Use a Pushbot's hardware
    Vision vision;
    PidConstants pids;
    Pid driveDistPid;
    Pid anglePid;
    ColorProvider colorProvider;

    public BaseAuto(int directionToJewels, int color, int shelfToFrontOrSide, ColorProvider colorProvider){
        this.colorProvider = colorProvider;
    }

    public int doSomethingWithColor(){
        if(colorProvider.getColor() == Color.BLUE){
            return 1;
        }
        return 0;
    }

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        vision = new Vision(hardwareMap);
        pids = new PidConstants();
        runVisionLoop();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.resetEncoders();

        robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.leftFrontMotor.getCurrentPosition(),
                robot.rightFrontMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        driveDistPid = pids.getPid(PidSubsystem.DRIVE);
        anglePid = pids.getPid(PidSubsystem.ANGLE);

        // Step through each leg of the path

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    private void runVisionLoop() {
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();


        vision.activateRelicTrackables();

        while (opModeIsActive()) {
            RelicRecoveryVuMark vuMark = vision.getColumn();
            if(vuMark != RelicRecoveryVuMark.UNKNOWN){
                OpenGLMatrix pose = vision.getPose(vuMark);
                telemetry.addData("VuMark", "%s visible", vuMark);

                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);
                // Extract the rotational components of the target relative to the robot
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;
                //TODO: do something with matrix
            }else{
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
