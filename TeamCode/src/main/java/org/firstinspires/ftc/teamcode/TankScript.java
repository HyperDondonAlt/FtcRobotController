package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.File;

import lombok.val;
import lombok.var;

@Autonomous(name = "Tank Script")
public class TankScript extends TankDrive {
    @Override
    public void runOpMode() {
        setup();

        val files = new File(Environment.getExternalStorageDirectory(), "TankScripts")
                .listFiles();

        StringBuilder availableScripts = new StringBuilder("\n");
        for (File file : files) {
            if (!file.getName().endsWith(".tscript")) continue;
            availableScripts.append(file.getName()).append("\n");
        }

        while (opModeIsActive()) {
            telemetry.addData("Available Scripts", availableScripts);
            telemetry.update();
        }


        val move = new AutonomousMove(
                2000,
                1,
                1
        );
        //move.execute(leftDrive, rightDrive);
    }
}
