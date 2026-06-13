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

        for (Map.Entry<?, ?> entry : script.configData.entrySet()) {
            if (entry.getValue() instanceof List) {
                sections.put((String) entry.getKey(), new Section((List<Map<String, ?>>) entry.getValue(), this, (String) entry.getKey()));
            } else {
                telemetry.addData("Unable to parse section", entry.getKey());
            }
        }

        if (!sections.containsKey("start")) throw new RuntimeException("Script must have a start section!");
    }

    public void execute() {
        sections.get("start").execute();
    }
}
