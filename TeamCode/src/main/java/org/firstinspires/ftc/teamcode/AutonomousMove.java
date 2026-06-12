package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AutonomousMove {
    @Getter
    final long time;
    @Getter
    final double leftSpeed;
    @Getter
    final double rightSpeed;

    void execute(DcMotor left, DcMotor right) {
        try {
            left.setPower(leftSpeed);
            right.setPower(rightSpeed);
            Thread.sleep(time);
        } catch (InterruptedException unused) {}

        left.setPower(0);
        right.setPower(0);
    }
}
