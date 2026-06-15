package org.firstinspires.ftc.teamcode.tankscript.script.Instruction;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class InstructionManager {
    @Getter
    private static HashMap<String, Instruction> instructions = new HashMap<>(Map.of(
            "move", MoveInstruction.getInstance(),
            "turn", TurnInstruction.getInstance(),
            "runSection", RunSectionInstruction.getInstance(),
            "wait", WaitInstruction.getInstance(),
            "telemetry", TelemetryInstruction.getInstance()
    ));
}
