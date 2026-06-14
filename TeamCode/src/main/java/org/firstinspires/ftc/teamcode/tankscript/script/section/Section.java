package org.firstinspires.ftc.teamcode.tankscript.script.section;

import android.util.Pair;
import lombok.val;
import lombok.var;
import org.firstinspires.ftc.teamcode.tankscript.script.Instruction.Instruction;
import org.firstinspires.ftc.teamcode.tankscript.script.Instruction.InstructionManager;
import org.firstinspires.ftc.teamcode.tankscript.script.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Section {
    private final Script context;
    private final ArrayList<Pair<Instruction, List<String>>> instructions = new ArrayList<>();

    public Section(List<Map<String, ?>> instructionList, Script context) {
        this.context = context;
        var i = 1;
        for (Map<String, ?> rawInstruction : instructionList) {
            Map.Entry<String, ?> entry = null;
            try {
                entry = (Map.Entry<String, ?>) rawInstruction.entrySet().toArray()[0];
            } catch (ClassCastException e) {
                context.getTelemetry().addData("Unable to parse line in section", "Line: " + i);
                context.getTelemetry().update();
            }

            val instruction = InstructionManager.getInstructions().get(entry.getKey());

            if (instruction == null) {
                context.getTelemetry().addData("Unable to find instruction", entry.getKey());
                context.getTelemetry().update();
            }

            val arguments = entry.getValue().toString()
                .replace(" ", "")
                .split(",");

            instructions.add(new Pair<>(instruction, Arrays.asList(arguments)));
            i++;
        }
    }


    public void execute() {
        for (Pair<Instruction, List<String>> instructionPair : instructions) {
            instructionPair.first.execute(instructionPair.second, context);
        }
    }
}
