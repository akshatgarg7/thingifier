package uk.co.compendiumdev.thingifier;

public class JsonOutputConfig {

    private boolean allowCompressedRelationships;
    private Boolean jsonOutputRelationshipsUsesIdsIfAvailable;

    public JsonOutputConfig(){
        allowCompressedRelationships=true;
        jsonOutputRelationshipsUsesIdsIfAvailable=true;
    }

    public Boolean doesAllowCompressedRelationships() {
        return allowCompressedRelationships;
    }

    public Boolean doesRelationshipsUseIdsIfAvailable() {
        return jsonOutputRelationshipsUsesIdsIfAvailable;
    }

    public void compressRelationships(final boolean config) {
        allowCompressedRelationships=config;
    }

    public void relationshipsUsesIdsIfAvailable(final boolean config) {
        jsonOutputRelationshipsUsesIdsIfAvailable=config;
    }


}