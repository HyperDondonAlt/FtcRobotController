package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.Getter;
import lombok.val;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

public final class RunSectionInstruction extends Instruction {
    @Getter
    private static RunSectionInstruction instance = new RunSectionInstruction();

    @Override
    public void execute(List<String> arguments, Script context) {
        val section = context.getSections().get(arguments.get(0));
        section.execute();
    }
}
