package com.heroku.java.CONTROLLER.Event;

import org.springframework.web.bind.annotation.GetMapping;

public class EventRegisterController {
    
    @GetMapping("/IEventRegister")
        public String IEventRegister(){
            return "IEventRegister";
    }
}
