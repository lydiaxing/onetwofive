package org.firstinspires.ftc.teamcode;

import pmtischlerLib.Pid;

public class PidConstants {
    double pAngle = 1;
    double iAngle = 1;
    double dAngle = 1;

    double pDist = 1;
    double iDist = 1;
    double dDist = 1;

    double integralMin = -1; //max and min of motor speed
    double integralMax = 1;

    public Pid getPid(PidSubsystem subsystem){
        Pid pid;

        if(subsystem == PidSubsystem.ANGLE){
            pid = new Pid(pAngle, iAngle, dAngle, integralMin, integralMax);
        }else{ //if it's a PidSubsystem.DRIVE
            pid = new Pid(pDist, iDist, dDist, integralMin, integralMax);
        }

        return pid;
    }
}
