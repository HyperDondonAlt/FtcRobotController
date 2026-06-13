package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.Getter;
import lombok.val;
import lombok.var;
import org.firstinspires.ftc.teamcode.tankscript.AutonomousMove;
import org.firstinspires.ftc.teamcode.tankscript.TankScript;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

public final class TurnInstruction extends Instruction {
    @Getter
    private static TurnInstruction instance = new TurnInstruction();

    @Override
    public void execute(List<String> arguments, Script context) {
        var leftSpeed = 0.0;

        if ("left".equals(arguments.get(0)))
            leftSpeed = Double.parseDouble(arguments.get(1));
        else if ("right".equals(arguments.get(0)))
            leftSpeed = -Double.parseDouble(arguments.get(1));

        val rightSpeed = -leftSpeed;

        long time = Long.parseLong(arguments.get(2));

        new AutonomousMove(
                time,
                leftSpeed,
                rightSpeed
        ).execute(TankScript.getLeftMotor(), TankScript.getRightMotor());
    }
}
