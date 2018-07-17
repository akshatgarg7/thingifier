package uk.co.compendiumdev.thingifier.application;

import uk.co.compendiumdev.thingifier.Thing;
import uk.co.compendiumdev.thingifier.Thingifier;
import uk.co.compendiumdev.thingifier.generic.FieldType;
import uk.co.compendiumdev.thingifier.generic.definitions.validation.VRule;
import uk.co.compendiumdev.thingifier.generic.definitions.Field;
import uk.co.compendiumdev.thingifier.generic.dsl.relationship.AndCall;
import uk.co.compendiumdev.thingifier.generic.dsl.relationship.Between;
import uk.co.compendiumdev.thingifier.generic.dsl.relationship.WithCardinality;
import uk.co.compendiumdev.thingifier.generic.instances.ThingInstance;

import static uk.co.compendiumdev.thingifier.generic.FieldType.STRING;

public class TodoManagerThingifier {

    public Thingifier get(){

        // this is basically an Entity Relationship diagram as source
        // TODO:  should expand functionality based on E-R diagrams
        // TODO: import thingifier from JSON to allow dynamic configuration
        // TODO: allow configuration from -defn FILENAME when starting at the command line
        Thingifier todoManager = new Thingifier();

        todoManager.setDocumentation("Todo Manager", "A Simple todo manager. All data lives in memory and is not persisted so the application is cleared everytime you start it. It does have some test data in here when you start");

        Thing todo = todoManager.createThing("todo", "todos");

        todo.definition()
                .addFields( Field.is("title", STRING).
                                        mandatory().
                                        withValidation(
                                                VRule.NotEmpty(),
                                                VRule.MatchesType()),
                            Field.is("description",STRING),
                            Field.is("doneStatus",FieldType.BOOLEAN).
                                        withDefaultValue("FALSE").
                                            withValidation(
                                                    VRule.MatchesType()))
                                    ;

        // TODO: validate against field type DATE
        // TODO: create SET field type
        // TODO: validate against set type
        // TODO: create validation rule MatchesFieldType to allow configuration of field validation against type
        // TODO: ValidationRule Interface - implemented by rules and ValidtionRuleGroup to allow processing in a list - main method validates()
        // TODO: create a MinimumLength validation rule
        // TODO: create a MaximumLength validation rule
        // TODO: create a IsGreaterThan validation rule
        // TODO: create an IsLessThan validation rule
        // TODO create a ValidationRuleGroup.and()  where all the validation rules in the group must pass for validation to pass
        // TODO create a ValidationRuleGroup.or()  where any of the validation rules in the group must pass for validation to pass - stops on first 'pass'
        // TODO : create a MatchesRegex validation rule for fields - this will be the simplest way of creating complex validation quickly
        // e.g. (.|\s)*\S(.|\s)*    non empty



        Thing project = todoManager.createThing("project", "projects");

        project.definition()
                .addFields(
                        Field.is("title", STRING),
                        Field.is("description",STRING),
                        Field.is("completed",FieldType.BOOLEAN).
                                withDefaultValue("FALSE").
                                withValidation(VRule.MatchesType()),
                        Field.is("active",FieldType.BOOLEAN).
                                withDefaultValue("TRUE").
                                withValidation(VRule.MatchesType()));


        Thing category = todoManager.createThing("category", "categories");

        category.definition()
                .addFields(
                        Field.is("title", STRING).
                                mandatory().
                                withValidation(VRule.NotEmpty()),
                        Field.is("description",STRING));

        // TODO create mandatory relationshisp = at the moment all entities can exist without relationship
        // e.g. create an estimate for a todo - the estimate must have a todo
        todoManager.defineRelationship(Between.things(project, todo), AndCall.it("tasks"), WithCardinality.of("1", "*")).
                whenReversed(WithCardinality.of("1","*"),AndCall.it("task-of"));

        todoManager.defineRelationship(Between.things(project, category), AndCall.it("categories"), WithCardinality.of("1", "*"));
        todoManager.defineRelationship(Between.things(category, todo), AndCall.it("todos"), WithCardinality.of("1", "*"));
        todoManager.defineRelationship(Between.things(category, project), AndCall.it("projects"), WithCardinality.of("1", "*"));
        todoManager.defineRelationship(Between.things(todo, category), AndCall.it("categories"), WithCardinality.of("1", "*"));


        // Some hard coded test data for experimenting with
        // TODO: allow importing from a JSON to create data in bulk
        ThingInstance paperwork = todo.createInstance().setValue("title", "scan paperwork");
        todo.addInstance(paperwork);

        ThingInstance filework = todo.createInstance().setValue("title", "file paperwork");
        todo.addInstance(filework);

        ThingInstance officeCategory = category.createInstance().setValue("title", "Office");

        ThingInstance homeCategory = category.createInstance().setValue("title", "Home");
        category.addInstance(homeCategory);

        ThingInstance officeWork = project.createInstance().setValue("title", "Office Work");
        project.addInstance(officeWork);

        officeWork.connects("tasks", paperwork);
        officeWork.connects("tasks", filework);

        paperwork.connects("categories", officeCategory);


        return todoManager;
    }
}
