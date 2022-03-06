package com.battlesnake.lokus.controller;

import com.battlesnake.codegen.models.*;
import com.battlesnake.lokus.service.MoveService;
import com.battlesnake.lokus.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class SnakeController {
  private final SettingsService settingsService;
  private final MoveService moveService;

  @GetMapping(value = "", produces = "application/json")
  public ResponseEntity<SettingsSchema> getCustomization(HttpServletRequest request){
      log.info("Request received => [Request URL: {}]", request.getRequestURL());
      return ResponseEntity.ok(settingsService.getSettings());
  }

  @PostMapping(value = "/start", produces = "application/json")
  public ResponseEntity<Boolean> postStart(@RequestBody POSTStartRequestSchema postStartRequestSchema){
      return ResponseEntity.ok(true);
  }

  @PostMapping(value = "/move", produces = "application/json")
  public ResponseEntity<POSTMoveResponseSchema> postMove(@RequestBody POSTMoveRequestSchema postMoveRequestSchema){
      return ResponseEntity.ok(moveService.decisionMaker(postMoveRequestSchema));
  }

  @PostMapping(value = "/end", produces = "application/json")
  public ResponseEntity<Boolean> postEnd(@RequestBody POSTEndResponseSchema postEndResponseSchema){
      return ResponseEntity.ok(true);
  }
}
