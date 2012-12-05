package com.missionhub.model.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MissionHubModelGenerator {

	public static final int VERSION = 1;
	public static final String PACKAGE = "com.missionhub.model";
	public static final String OUT_DIR = "../missionhub-android/src";
	
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(VERSION, PACKAGE);
		schema.enableKeepSectionsByDefault();
		
		/**
		 * User/Person
		 */
		
		Entity user = schema.addEntity("User");
		user.addIdProperty();
		user.addStringProperty("username");
		user.addStringProperty("locale");
		Property userPersonId = user.addLongProperty("person_id").getProperty();
		user.addLongProperty("primary_organization_id");
		user.addStringProperty("time_zone");
		user.addDateProperty("updated_at");
		user.addDateProperty("created_at");
		user.addDateProperty("deleted_at");
		
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
		person.addToOne(user, person.addLongProperty("user_id").getProperty());
		user.addToOne(person, userPersonId);
		person.addLongProperty("fb_uid");
		person.addDateProperty("updated_at");
		person.addDateProperty("created_at");
		person.addDateProperty("deleted_at");
		
		Entity emailAddress = schema.addEntity("EmailAddress");
		emailAddress.addIdProperty();
		emailAddress.addStringProperty("email");
		emailAddress.addBooleanProperty("primary");
		Property emailAddressPersonId = emailAddress.addLongProperty("person_id").getProperty();
		emailAddress.addToOne(person, emailAddressPersonId);
		person.addToMany(emailAddress, emailAddressPersonId);		
		emailAddress.addDateProperty("updated_at");
		emailAddress.addDateProperty("created_at");
		emailAddress.addDateProperty("deleted_at");
		
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
		phoneNumber.addDateProperty("deleted_at");
		
		/**
		 * Organizations/Roles
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
		organization.addDateProperty("deleted_at");
		
		Entity role = schema.addEntity("Role");
		role.addIdProperty();
		role.addStringProperty("name");
		role.addToOne(organization, role.addLongProperty("organization_id").getProperty());
		role.addStringProperty("i18n");
		role.addDateProperty("updated_at");
		role.addDateProperty("created_at");
		role.addDateProperty("deleted_at");
		
		Entity organizationalRole = schema.addEntity("OrganizationalRole");
		organizationalRole.addIdProperty();
		organizationalRole.addStringProperty("followup_status");
		Property organizationRolePersonId = organizationalRole.addLongProperty("person_id").getProperty();
		organizationalRole.addToOne(phoneNumber, organizationRolePersonId);
		person.addToMany(organizationalRole, organizationRolePersonId);
		organizationalRole.addToOne(organization, organizationalRole.addLongProperty("organization_id").getProperty());
		Property organizationRoleRoleId = organizationalRole.addLongProperty("role_id").getProperty();
		organizationalRole.addToOne(role, organizationRoleRoleId);
		role.addToMany(organizationalRole, organizationRoleRoleId);
		organizationalRole.addDateProperty("start_date");
		organizationalRole.addDateProperty("end_date");
		organizationalRole.addBooleanProperty("deleted");
		organizationalRole.addDateProperty("archive_date");
		organizationalRole.addDateProperty("updated_at");
		organizationalRole.addDateProperty("created_at");
		organizationalRole.addDateProperty("deleted_at");
		
		Entity contactAssignment = schema.addEntity("ContactAssignment");
		contactAssignment.addIdProperty();
		Property contactAssignmentPersonId = contactAssignment.addLongProperty("person_id").getProperty();
		contactAssignment.addToOne(person, contactAssignmentPersonId, "person");
		person.addToMany(contactAssignment, contactAssignmentPersonId, "assigned_to");
		Property contactAssignmentAssignedToId = contactAssignment.addLongProperty("assigned_to_id").getProperty();
		contactAssignment.addToOne(person, contactAssignmentAssignedToId, "person_assigned_to");
		person.addToMany(contactAssignment, contactAssignmentAssignedToId, "assigned_to_me");
		contactAssignment.addToOne(organization, contactAssignment.addLongProperty("organziation_id").getProperty());
		contactAssignment.addDateProperty("updated_at");
		contactAssignment.addDateProperty("created_at");
		contactAssignment.addDateProperty("deleted_at");
		
		/**
		 * Groups
		 */
		
		Entity group = schema.addEntity("Group");
		group.setTableName("Groups"); // group is a reserved word
		group.addIdProperty();
		group.addStringProperty("name");
		group.addStringProperty("location");
		group.addStringProperty("meets");
		group.addStringProperty("meeting_day");
		group.addIntProperty("start_time");
		group.addIntProperty("end_time");
		Property groupOrganizationId = group.addLongProperty("organization_id").getProperty();
		group.addToOne(organization, groupOrganizationId);
		organization.addToMany(group, groupOrganizationId);
		group.addBooleanProperty("list_publicly");
		group.addBooleanProperty("approve_join_requests");
		group.addDateProperty("updated_at");
		group.addDateProperty("created_at");
		group.addDateProperty("deleted_at");
		
		/**
		 * Comments
		 */
		
		Entity followupComment = schema.addEntity("FollowupComment");
		followupComment.addIdProperty();
		Property followupCommentContactId = followupComment.addLongProperty("contact_id").getProperty();
		followupComment.addToOne(person, followupCommentContactId, "contact");
		person.addToMany(followupComment, followupCommentContactId, "comments_on_me");
		Property followupCommentCommenterId = followupComment.addLongProperty("commenter_id").getProperty();
		followupComment.addToOne(person, followupCommentCommenterId, "commenter");
		person.addToMany(followupComment, followupCommentCommenterId, "followup_comments");
		followupComment.addToOne(organization, followupComment.addLongProperty("organization_id").getProperty());
		followupComment.addStringProperty("comment");
		followupComment.addStringProperty("status");
		followupComment.addDateProperty("updated_at");
		followupComment.addDateProperty("created_at");
		followupComment.addDateProperty("deleted_at");
		
		Entity rejoicable = schema.addEntity("Rejoicable");
		rejoicable.addIdProperty();
		rejoicable.addStringProperty("what");
		Property rejoicablePersonId = rejoicable.addLongProperty("person_id").getProperty();
		rejoicable.addToOne(person, rejoicablePersonId);
		Property rejoicableCreatedById = rejoicable.addLongProperty("created_by_id").getProperty();
		rejoicable.addToOne(phoneNumber, rejoicableCreatedById);
		person.addToMany(rejoicable, rejoicableCreatedById);
		Property rejoicableCommentId = rejoicable.addLongProperty("followup_comment_id").getProperty();
		rejoicable.addToOne(followupComment, rejoicableCommentId);
		followupComment.addToMany(rejoicable, rejoicableCommentId, "rejoicables");
		rejoicable.addToOne(organization, rejoicable.addLongProperty("organization_id").getProperty());
		rejoicable.addDateProperty("updated_at");
		rejoicable.addDateProperty("created_at");
		rejoicable.addDateProperty("deleted_at");
		
		/**
		 * Surveys
		 */
		
		Entity question = schema.addEntity("Question");
		question.addIdProperty();
		question.addStringProperty("kind");
		question.addStringProperty("style");
		question.addStringProperty("label");
		question.addStringProperty("content");
		question.addStringProperty("person");
		question.addStringProperty("object_name");
		question.addStringProperty("attribute_name");
		question.addBooleanProperty("web_only");
		question.addStringProperty("trigger_words");
		question.addStringProperty("notify_via");
		question.addBooleanProperty("hidden");
		question.addDateProperty("updated_at");
		question.addDateProperty("created_at");
		question.addDateProperty("deleted_at");
		
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
		survey.addDateProperty("deleted_at");
		
		Entity smsKeyword = schema.addEntity("SmsKeyword");
		smsKeyword.addIdProperty();
		smsKeyword.addStringProperty("keyword");
		Property smsKeywordOrganizationId = smsKeyword.addLongProperty("organization_id").getProperty();
		smsKeyword.addToOne(organization, smsKeywordOrganizationId);
		organization.addToMany(smsKeyword, smsKeywordOrganizationId, "keywords");
		person.addToMany(smsKeyword, smsKeyword.addLongProperty("user_id").getProperty());
		smsKeyword.addStringProperty("explanation");
		smsKeyword.addStringProperty("state");
		smsKeyword.addStringProperty("initial_response");
		Property smsKeywordSurveyId = smsKeyword.addLongProperty("survey_id").getProperty();
		smsKeyword.addToOne(survey, smsKeywordSurveyId);
		survey.addToMany(smsKeyword, smsKeywordSurveyId);
		smsKeyword.addDateProperty("updated_at");
		smsKeyword.addDateProperty("created_at");
		smsKeyword.addDateProperty("deleted_at");
		
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