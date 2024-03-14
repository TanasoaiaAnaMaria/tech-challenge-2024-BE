package com.usv.Team.Finder.App.repository;

public interface ApplicationConstants {
    String REGISTRATION_EMPLOYEE_ERROR = "Invitation not found or invalid";
    String EMAIL_ALREADY_EXISTS = "E-mail address already in use";
    String REGISTRATION_INVITATION_EXPIRED_ERROR = "Invitation expired. Contact your organisation admin.";
    String REGISTRATION_EMPLOYEE_ALREADY_EXIST="Already registered";
    String ERROR_MESSAGE_ORGANISATION = "Organisation does not exist";
    String ERROR_MESSAGE_DEPARTMENT = "Department does not exist";
    String ERROR_MESSAGE_USER = "User does not exist";
    String ERROR_MESSAGE_ROLE = "Role does not exist";
    String ERROR_MESSAGE_TEAMROLE = "Team role does not exist";
    String ERROR_MESSAGE_SKILL_CATEGORY = "Skill category does not exist";
    String ERROR_USERS_FROM_ORGANISATION = "No users in organisation";
    String ERROR_ROLE_NOT_FOUND_FOR_USER = "Specified role not found for user.";
    String ERROR_USER_ALREADY_ASSIGNED_TO_A_DEPARTMENT = "User is already assigned to a department";
    String ERROR_NO_RIGHTS = "You have no rights to add entities to another organisation";
    String ERROR_NULL_PARAMETER = "The received parameter must not be null";
}
