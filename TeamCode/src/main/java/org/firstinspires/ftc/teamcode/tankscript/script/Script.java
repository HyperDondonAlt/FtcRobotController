package org.firstinspires.ftc.teamcode.tankscript.script;

import lombok.Getter;
import lombok.val;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tankscript.script.section.Section;
import org.firstinspires.ftc.teamcode.tankscript.yaml.Yaml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Script {
    @Getter
    HashMap<String, Section> sections = new HashMap<>();

    @Getter
    private Telemetry telemetry;

    public Script(File scriptFile, Telemetry telemetry) throws Exception {
        this.telemetry = telemetry;

        val script = new Yaml(scriptFile);

        // key = name
        // value = values/instructions
        for (Map.Entry<String, ?> section : script.configData.entrySet()) {
            if (section.getValue() instanceof List) {
                sections.put(section.getKey(), new Section((List<Map<String, ?>>) section.getValue(), this));
            } else {
                telemetry.addData("Unable to parse section", section.getKey());
            }
        }

        if (!sections.containsKey("start")) throw new RuntimeException("Script must have a start section!");
    }

    public void execute() {
        sections.get("start").execute();
    }
}
