// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.RomiDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.xbox;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final RomiDrivetrain m_romiDrivetrain = new RomiDrivetrain();

  // private final ExampleCommand m_autoCommand = new ExampleCommand(m_romiDrivetrain);
  
  private final XboxController m_controller = new XboxController(xbox.xBoxUSBport);

  // autonomous
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // sets up arcade drive as the default for the Romi
    m_romiDrivetrain.setDefaultCommand(new RunCommand(
      ()-> m_romiDrivetrain.arcadeDrive(
        -m_controller.getRawAxis(xbox.xBoxMove),
        m_controller.getRawAxis(xbox.xBoxTurn)
        ),
      m_romiDrivetrain
      )
    );

    // auto option
    m_chooser.addOption("20 cm", new FunctionalCommand(
      m_romiDrivetrain::resetEncoders,
      ()-> m_romiDrivetrain.arcadeDrive(0.35, 0.0), 
      interrupted -> m_romiDrivetrain.arcadeDrive(0.0, 0.0),
      () -> m_romiDrivetrain.getLeftDistanceInch() >= 8.0,
      m_romiDrivetrain
      )
    );

    // auto option
    m_chooser.setDefaultOption("2 seconds", new RunCommand(
      ()-> m_romiDrivetrain.arcadeDrive(0.6, 0.0), 
      m_romiDrivetrain)
      .withTimeout(2.0)
    );

    // auto to dashboard
    SmartDashboard.putData(m_chooser);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  }

  
}
