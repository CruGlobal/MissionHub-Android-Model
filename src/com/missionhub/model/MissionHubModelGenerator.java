package com.missionhub.model;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MissionHubModelGenerator {

    public static final int VERSION = 2;
    public static final String PACKAGE = "com.missionhub.model";
	public static final String OUT_DIR = "../MissionHub-Android/app/src";
	
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(VERSION, PACKAGE);
        schema.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();
		
		/**
		 * User/Person
		 */
		
		Entity user = schema.addEntity("User");
		user.addIdProperty();
		Property userPersonId = user.addLongProperty("person_id").getProperty();
		user.addLongProperty("primary_organization_id");
		user.addDateProperty("updated_at");
		user.addDateProperty("created_at");
		
		Entity person = schema.addEntity("Person");
		person.addIdProperty();
		person.addStringProperty("first_name");
		person.addStringProperty("last_name");
		person.addStringProperty("gender");
		person.addStringProperty("campus");
		person.addStringProperty("year_in_school");
		person.addStringProperty("major");
		person.addStringProperty("minor");
		person.addDateProperty("birth_date");
		person.addDateProperty("date_became_christian");
		person.addDateProperty("graduation_date");
		person.addStringProperty("picture");
		person.addToOne(user, person.addLongProperty("user_id").getProperty());
		user.addToOne(person, userPersonId);
		person.addLongProperty("fb_uid");
		person.addDateProperty("updated_at");
		person.addDateProperty("created_at");
		
		Entity address = schema.addEntity("Address");
		address.addIdProperty();
		Property addressPersonId = address.addLongProperty("person_id").getProperty();
		address.addStringProperty("address1");
		address.addStringProperty("address2");
		address.addStringProperty("city");
		address.addStringProperty("state");
		address.addStringProperty("country");
		address.addStringProperty("zip");
        address.addStringProperty("address_type");
        address.addToOne(person, addressPersonId);
		person.addToMany(address, addressPersonId);
		
		Entity emailAddress = schema.addEntity("EmailAddress");
		emailAddress.addIdProperty();
		emailAddress.addStringProperty("email");
		emailAddress.addBooleanProperty("primary");
		Property emailAddressPersonId = emailAddress.addLongProperty("person_id").getProperty();
		emailAddress.addToOne(person, emailAddressPersonId);
		person.addToMany(emailAddress, emailAddressPersonId);		
		emailAddress.addDateProperty("updated_at");
		emailAddress.addDateProperty("created_at");
		
		Entity phoneNumber = schema.addEntity("PhoneNumber");
		phoneNumber.addIdProperty();
		phoneNumber.addStringProperty("number");
		phoneNumber.addStringProperty("location");
		phoneNumber.addBooleanProperty("primary");
		phoneNumber.addStringProperty("txt_to_email");
		phoneNumber.addDateProperty("email_updated_at");
		Property phoneNumberPersonId = phoneNumber.addLongProperty("person_id").getProperty();
		phoneNumber.addToOne(person, phoneNumberPersonId);
		person.addToMany(phoneNumber, phoneNumberPersonId);
		phoneNumber.addDateProperty("updated_at");
		phoneNumber.addDateProperty("created_at");
		
		/**
         * Organizations/Roles/Permissions
         */
		
		Entity organization = schema.addEntity("Organization");
		organization.addIdProperty();
		organization.addStringProperty("name");
		organization.addStringProperty("terminology");
		organization.addStringProperty("ancestry");
		organization.addBooleanProperty("show_sub_orgs");
		organization.addStringProperty("status");
		organization.addDateProperty("updated_at");
		organization.addDateProperty("created_at");

        Entity label = schema.addEntity("Label");
        label.addIdProperty();
        label.addStringProperty("name");
        Property labelOrganizationId = label.addLongProperty("organization_id").getProperty();
        label.addToOne(organization, labelOrganizationId);
        organization.addToMany(label, labelOrganizationId);
        label.addStringProperty("i18n");
        label.addDateProperty("updated_at");
        label.addDateProperty("created_at");

        Entity permission = schema.addEntity("Permission");
        permission.addIdProperty();
        permission.addStringProperty("name");
        permission.addStringProperty("i18n");
        permission.addDateProperty("updated_at");
        permission.addDateProperty("created_at");

        Entity organizationalLabel = schema.addEntity("OrganizationalLabel");
        organizationalLabel.addIdProperty();
        Property organizationRolePersonId = organizationalLabel.addLongProperty("person_id").getProperty();
        organizationalLabel.addToOne(person, organizationRolePersonId);
        person.addToMany(organizationalLabel, organizationRolePersonId);
        organizationalLabel.addToOne(organization, organizationalLabel.addLongProperty("organization_id").getProperty());
        organizationalLabel.addToOne(person, organizationalLabel.addLongProperty("added_by_id").getProperty(), "addedByPerson");
        organizationalLabel.addToOne(label, organizationalLabel.addLongProperty("label_id").getProperty());
        organizationalLabel.addDateProperty("start_date");
        organizationalLabel.addDateProperty("updated_at");
        organizationalLabel.addDateProperty("created_at");
        organizationalLabel.addDateProperty("removed_date");

        Entity organizationalPermission = schema.addEntity("OrganizationalPermission");
        organizationalPermission.addIdProperty();
        Property organizationalPermissionPersonId = organizationalPermission.addLongProperty("person_id").getProperty();
        organizationalPermission.addToOne(person, organizationalPermissionPersonId);
        person.addToMany(organizationalPermission, organizationalPermissionPersonId);
        organizationalPermission.addToOne(permission, organizationalPermission.addLongProperty("permission_id").getProperty());
        organizationalPermission.addToOne(organization, organizationalPermission.addLongProperty("organization_id").getProperty());
        organizationalPermission.addStringProperty("followup_status");
        organizationalPermission.addDateProperty("start_date");
        organizationalPermission.addDateProperty("updated_at");
        organizationalPermission.addDateProperty("created_at");
        organizationalPermission.addDateProperty("archive_date");

        Entity contactAssignment = schema.addEntity("ContactAssignment");
		contactAssignment.addIdProperty();
		Property contactAssignmentPersonId = contactAssignment.addLongProperty("person_id").getProperty();
		contactAssignment.addToOne(person, contactAssignmentPersonId, "person");
		person.addToMany(contactAssignment, contactAssignmentPersonId, "assigned_to");
		Property contactAssignmentAssignedToId = contactAssignment.addLongProperty("assigned_to_id").getProperty();
		contactAssignment.addToOne(person, contactAssignmentAssignedToId, "person_assigned_to");
		person.addToMany(contactAssignment, contactAssignmentAssignedToId, "assigned_to_me");
		contactAssignment.addToOne(organization, contactAssignment.addLongProperty("organization_id").getProperty());
		contactAssignment.addDateProperty("updated_at");
		contactAssignment.addDateProperty("created_at");

        /**
         * Interactions
         */

        Entity interaction = schema.addEntity("Interaction");
        interaction.addIdProperty();
        Property interactionTypeId = interaction.addLongProperty("interaction_type_id").getProperty();
        Property interactionReceiverId = interaction.addLongProperty("receiver_id").getProperty();
        interaction.addToOne(person, interactionReceiverId, "receiver");
        person.addToMany(interaction, interactionReceiverId, "receivedInteractions");
        Property interactionCreatedById = interaction.addLongProperty("created_by_id").getProperty();
        interaction.addToOne(person, interactionCreatedById, "creator");
        person.addToMany(interaction, interactionCreatedById, "createdInteractions");
        Property interactionUpdatedById = interaction.addLongProperty("updated_by_id").getProperty();
        interaction.addToOne(person, interactionUpdatedById, "updater");
        person.addToMany(interaction, interactionUpdatedById, "updatedInteractions");
        interaction.addStringProperty("comment");
        interaction.addStringProperty("privacy_setting");
        interaction.addDateProperty("timestamp");

        Entity interactionInitiator = schema.addEntity("InteractionInitiator");
        interactionInitiator.addIdProperty();
        Property interactionInitiatorPersonId = interactionInitiator.addLongProperty("person_id").getProperty();
        person.addToMany(interactionInitiator, interactionInitiatorPersonId);
        interactionInitiator.addToOne(person, interactionInitiatorPersonId);
        Property interactionInitiatorInteractionId = interactionInitiator.addLongProperty("interaction_id").getProperty();
        interaction.addToMany(interactionInitiator, interactionInitiatorInteractionId);
        interactionInitiator.addToOne(interaction, interactionInitiatorInteractionId);
        interactionInitiator.addDateProperty("updated_at");
        interactionInitiator.addDateProperty("created_at");

        Entity interactionType = schema.addEntity("InteractionType");
        interactionType.addIdProperty();
        Property interactionTypeOrganizationId = interactionType.addLongProperty("organization_id").getProperty();
        organization.addToMany(interactionType, interactionTypeOrganizationId);
        interactionType.addToOne(organization, interactionTypeOrganizationId);
        interactionType.addStringProperty("name");
        interactionType.addStringProperty("i18n");
        interactionType.addStringProperty("icon");
        interactionType.addDateProperty("updated_at");
        interactionType.addDateProperty("created_at");
        interaction.addToOne(interactionType, interactionTypeId);

        /**
         * Surveys
         */
		
		Entity question = schema.addEntity("Question");
		question.addIdProperty();
		question.addStringProperty("kind");
		question.addStringProperty("style");
		question.addStringProperty("label");
		question.addStringProperty("content");
		question.addStringProperty("object_name");
		question.addStringProperty("attribute_name");
		question.addBooleanProperty("web_only");
		question.addStringProperty("trigger_words");
		question.addStringProperty("notify_via");
		question.addBooleanProperty("hidden");
		question.addDateProperty("updated_at");
		question.addDateProperty("created_at");
		
		Entity survey = schema.addEntity("Survey");
		survey.addIdProperty();
		survey.addStringProperty("title");
		Property surveyOrganizationId = survey.addLongProperty("organization_id").getProperty();
		survey.addToOne(organization, surveyOrganizationId);
		organization.addToMany(survey, surveyOrganizationId, "surveys");
		survey.addStringProperty("post_survey_message");
		survey.addStringProperty("terminology");
		survey.addStringProperty("login_paragraph");
		survey.addBooleanProperty("is_frozen");
		survey.addDateProperty("updated_at");
		survey.addDateProperty("created_at");
		
		Entity smsKeyword = schema.addEntity("SmsKeyword");
		smsKeyword.addIdProperty();
		smsKeyword.addStringProperty("keyword");
		Property smsKeywordOrganizationId = smsKeyword.addLongProperty("organization_id").getProperty();
		smsKeyword.addToOne(organization, smsKeywordOrganizationId);
		organization.addToMany(smsKeyword, smsKeywordOrganizationId, "keywords");
		user.addToMany(smsKeyword, smsKeyword.addLongProperty("user_id").getProperty());
		smsKeyword.addStringProperty("explanation");
		smsKeyword.addStringProperty("state");
		smsKeyword.addStringProperty("initial_response");
		Property smsKeywordSurveyId = smsKeyword.addLongProperty("survey_id").getProperty();
		smsKeyword.addToOne(survey, smsKeywordSurveyId);
		survey.addToMany(smsKeyword, smsKeywordSurveyId);
		smsKeyword.addDateProperty("updated_at");
		smsKeyword.addDateProperty("created_at");
		
		Entity answerSheet = schema.addEntity("AnswerSheet");
		answerSheet.addIdProperty();
		Property answerSheetPersonId = answerSheet.addLongProperty("person_id").getProperty();
		answerSheet.addToOne(person, answerSheetPersonId);
		person.addToMany(answerSheet, answerSheetPersonId);
		answerSheet.addToOne(survey, answerSheet.addLongProperty("survey_id").getProperty());
		answerSheet.addDateProperty("created_at");
		answerSheet.addDateProperty("updated_at");
		answerSheet.addDateProperty("completed_at");		
		
		Entity answer = schema.addEntity("Answer");
		answer.addIdProperty();
		Property answerAnswerSheetId = answer.addLongProperty("answer_sheet_id").getProperty();
		answer.addToOne(answerSheet, answerAnswerSheetId);
		answerSheet.addToMany(answer, answerAnswerSheetId);
		answer.addToOne(question, answer.addLongProperty("question_id").getProperty());
		answer.addStringProperty("value");
		
		/**
		 * Settings
		 */
		
		Entity settings  = schema.addEntity("Setting");
		settings.addIdProperty().autoincrement();
		settings.addStringProperty("key");
		settings.addStringProperty("value");
		
		Entity usersettings = schema.addEntity("UserSetting");
		usersettings.addIdProperty().autoincrement();
		usersettings.addToOne(person, usersettings.addLongProperty("person_id").getProperty());
		usersettings.addStringProperty("key");
		usersettings.addStringProperty("value");

		new DaoGenerator().generateAll(schema, OUT_DIR);
	}
}
