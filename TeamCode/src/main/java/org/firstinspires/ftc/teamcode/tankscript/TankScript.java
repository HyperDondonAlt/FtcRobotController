package org.firstinspires.ftc.teamcode.tankscript;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.hardware.DcMotor;
import lombok.Getter;
import lombok.val;
import lombok.var;
import org.firstinspires.ftc.teamcode.TankDrive;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

@Autonomous(name = "Tank Script")
public class TankScript extends TankDrive {
    @Getter
    private static DcMotor leftMotor;
    @Getter
    private static DcMotor rightMotor;

    @Override
    public void runOpMode() {
        setup();

        leftMotor = leftDrive;
        rightMotor = rightDrive;

        val files = new File(Environment.getExternalStorageDirectory(), "TankScripts")
                .listFiles();

        List<File> scripts = new ArrayList<>();
        StringBuilder availableScripts = new StringBuilder("\n");
        for (File file : files) {
            if (!file.getName().endsWith(".tscript")) continue;
            scripts.add(file);
            availableScripts.append(file.getName().replace(".tscript", "")).append("\n");
        }

        var selectedScript = 0;
        var aPressed = false;
        var bPressed = false;


        while (opModeIsActive()) {
            if (!gamepad1.a) aPressed = false;
            if (!gamepad1.b) bPressed = false;

            telemetry.addData("Available Scripts", availableScripts);

            if (gamepad1.a && !aPressed) {
                aPressed = true;
                selectedScript++;

                if (selectedScript == scripts.size()) selectedScript = 0;
            }

            if (gamepad1.b && !bPressed) {
                bPressed = true;
                try {
                    val script = new Script(scripts.get(selectedScript), telemetry);
                    script.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            telemetry.addData("Selected Script", scripts.get(selectedScript).getName().replace(".tscript", ""));

            telemetry.update();
        }
    }
}
