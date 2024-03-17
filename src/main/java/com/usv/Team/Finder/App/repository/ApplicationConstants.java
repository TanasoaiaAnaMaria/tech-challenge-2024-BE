package com.usv.Team.Finder.App.repository;

public interface ApplicationConstants {
    String REGISTRATION_EMPLOYEE_ERROR = "Invitation not found or invalid";
    String EMAIL_ALREADY_EXISTS = "E-mail address already in use";
    String REGISTRATION_INVITATION_EXPIRED_ERROR = "Invitation expired. Contact your organisation admin.";
    String REGISTRATION_EMPLOYEE_ALREADY_EXIST="Already registered";
    String ERROR_MESSAGE_ORGANISATION = "Organisation does not exist";
    String ERROR_MESSAGE_DEPARTMENT = "Department does not exist";
    String ERROR_ALREADY_DM = "You are already assigned as department manager to another department";
    String ERROR_MESSAGE_USER = "User does not exist";
    String ERROR_MESSAGE_ROLE = "Role does not exist";
    String ERROR_MESSAGE_TEAMROLE = "Team role does not exist";
    String ERROR_MESSAGE_SKILL_CATEGORY = "Skill category does not exist";
    String ERROR_MESSAGE_USER_SKILL = "User skill does not exist";
    String ERROR_MESSAGE_NO_NOTIFICATIONS = "No notification";
    String ERROR_MESSAGE_NOTIFICATION_NOT_FOUND = "Notification not exist";
    String ERROR_MESSAGE_SKILL = "Skill does not exist";
    String ERROR_USERS_FROM_ORGANISATION = "No users in organisation";
    String ERROR_ROLE_NOT_FOUND_FOR_USER = "Specified role not found for user.";
    String ERROR_USER_ALREADY_ASSIGNED_TO_A_DEPARTMENT = "User is already assigned to a department";
    String ERROR_NO_RIGHTS = "You have no rights to add entities to another organisation";
    String ERROR_NULL_PARAMETER = "The received parameter must not be null";
    String ERROR_UPDATE_SKILL = "Unauthorized to update skill";
    String ERROR_DELETE_SKILL = "Unauthorized to delete skill";
    String ERROR_SKILL_IN_USE_BY_DEPARTMENTS = "Skill cannot be deleted because it is used in departments";
    String ERROR_NOT_DEPARTMENT_MANAGER = "You are not authorized to perform this action as you are not a department manager.";
    String ERROR_SKILL_ALREADY_IN_DEPARTMENT = "This skill is already added to your department.";
    String ERROR_SKILL_NOT_IN_DEPARTMENT = "This skill is not part of your department.";
}
