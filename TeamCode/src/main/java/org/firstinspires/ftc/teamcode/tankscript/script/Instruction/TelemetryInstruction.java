package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import org.firstinspires.ftc.teamcode.tankscript.TankScript;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

import lombok.Getter;
import lombok.val;

public final class TelemetryInstruction extends Instruction {
    @Getter
    private static TelemetryInstruction instance = new TelemetryInstruction();

    @Override
    public void execute(List<String> arguments, Script context) {
        TankScript.log(arguments.get(0), arguments.get(1));
    }
}
