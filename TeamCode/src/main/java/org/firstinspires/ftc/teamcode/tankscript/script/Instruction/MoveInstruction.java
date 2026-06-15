package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.Getter;
import org.firstinspires.ftc.teamcode.tankscript.AutonomousMove;
import org.firstinspires.ftc.teamcode.tankscript.TankScript;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

public final class MoveInstruction extends Instruction {
    @Getter
    private static MoveInstruction instance = new MoveInstruction();

    @Override
    public void execute(List<String> arguments, Script context) {
        double leftSpeed = 0;
        double rightSpeed = 0;
        long time = 0;

        if (arguments.size() == 2) {
            leftSpeed = -Double.parseDouble(arguments.get(0));
            rightSpeed = leftSpeed;
            time = Long.parseLong(arguments.get(1));
        } else if (arguments.size() == 3) {
            leftSpeed = -Double.parseDouble(arguments.get(0));
            rightSpeed = -Double.parseDouble(arguments.get(1));
            time = Long.parseLong(arguments.get(2));
        }

        new AutonomousMove(
                time,
                leftSpeed,
                rightSpeed
        ).execute(TankScript.getLeftMotor(), TankScript.getRightMotor());
    }
}
