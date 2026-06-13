package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.AllArgsConstructor;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.List;

@AllArgsConstructor
public abstract class Instruction {
    public abstract void execute(List<String> arguments, Script context);
}
