package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.Getter;
import lombok.val;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

public final class WaitInstruction extends Instruction {
    @Getter
    private static WaitInstruction instance = new WaitInstruction();

    @Override
    public void execute(List<String> arguments, Script context) {
        try {
            val length = Long.parseLong(arguments.get(0));
            Thread.sleep(length);
        } catch (InterruptedException ignored) {}
    }
}
