package uk.co.compendiumdev.thingifier.core.domain.definitions;

import uk.co.compendiumdev.thingifier.core.domain.definitions.relationship.RelationshipDefinition;
import uk.co.compendiumdev.thingifier.core.domain.definitions.relationship.RelationshipVectorDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ERSchema {

    private final ConcurrentHashMap<String, RelationshipDefinition> relationships;
    private final ConcurrentHashMap<String, EntityDefinition> entityDefinitions;

    public ERSchema(){
        relationships = new ConcurrentHashMap<>();
        entityDefinitions = new ConcurrentHashMap<>();
    }

    public EntityDefinition defineEntity(final String thingName, final String pluralName) {
        EntityDefinition definition = new EntityDefinition(thingName, pluralName);
        entityDefinitions.put(definition.getName(), definition);
        return definition;
    }

    public Collection<RelationshipDefinition> getRelationships() {
        return relationships.values();
    }

    public Collection<EntityDefinition> getEntityDefinitions() {
        return entityDefinitions.values();
    }
    public RelationshipDefinition defineRelationship(final EntityDefinition from, final EntityDefinition to, final String named, final Cardinality of) {
        RelationshipDefinition relationship =
                RelationshipDefinition.create(
                        new RelationshipVectorDefinition(
                                from,
                                named,
                                to,
                                of));
        relationships.put(named, relationship);
        return relationship;
    }

    public boolean hasRelationshipNamed(final String relationshipName) {
        if (relationships.containsKey(relationshipName.toLowerCase())) {
            return true;
        }

        // perhaps it is a reverse relationship?
        for (RelationshipDefinition defn : relationships.values()) {
            if (defn.isTwoWay()) {
                if (defn.getReversedRelationship().getName().equalsIgnoreCase(relationshipName)) {
                    return true;
                }
            }
        }

        return false;
    }


    public List<String> getEntityNames() {
        List<String> names = new ArrayList();
        names.addAll(entityDefinitions.keySet());
        return names;
    }

    public boolean hasEntityNamed(final String aName) {
        return entityDefinitions.containsKey(aName);
    }

    public boolean hasEntityWithPluralNamed(final String term) {
        return getDefinitionWithPluralNamed(term)!=null;
    }

    public EntityDefinition getEntityDefinitionNamed(final String term) {
        if(entityDefinitions.containsKey(term)){
            return entityDefinitions.get(term);
        }
        return null;
    }

    public EntityDefinition getDefinitionWithPluralNamed(final String term) {
        for(EntityDefinition defn : entityDefinitions.values()){
            if(defn.getPlural().equalsIgnoreCase(term)){
                return defn;
            }
        }
        return null;
    }

    public EntityDefinition getDefinitionWithSingularOrPluralNamed(final String term) {
        final EntityDefinition defn = getEntityDefinitionNamed(term);
        if(defn!=null){
            return defn;
        }

        // look for plural
        return getDefinitionWithPluralNamed(term);
    }


}
