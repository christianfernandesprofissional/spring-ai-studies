package com.springAi.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTools.class);

    //--Esta anotation é essencial, é através dela que a LLM sabe da existencia da ferramenta, ela só enxerga o nome,
    //--e julga através da descrição se a ferramenta irá ou não ajudar a resolver algum problema
    //--O nome pode ser qualquer um, mas é uma boa prática coincidir com o que a ferramenta faz
    @Tool(name="getCurrentLocalTime", description = "Get the current time in the user's timezone")
    public String getCurrentLocalTime() {
        LOGGER.info("Returning the current time in the user's timezone");
        System.out.println(LocalTime.now().toString());
        return LocalTime.now().toString();
    }

    @Tool(name = "getCurrentTime",
            description = "Get the current time in the specified time zone.")
    public String getCurrentTime(@ToolParam(
            description = "Value representing the time zone") String timeZone) {
        LOGGER.info("Returning the current time in the timezone {}", timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}