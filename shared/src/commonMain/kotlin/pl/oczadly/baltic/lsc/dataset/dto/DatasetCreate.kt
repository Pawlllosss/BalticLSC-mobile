package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

//{"name":"Test data set","dataTypeUid":"dd-001-000","dataTypeName":"DataFile","dataTypeVersion":"1.0","multiplicity":0,"dataStructureUid":"dd-003-002","dataStructureName":"ComputationParams","dataStructureVersion":"1.0","accessTypeUid":"dd-004-000","accessTypeName":"NoSQL_DB","accessTypeVersion":"1.0","values":"{\n\t\"ResourcePath\": \"/sasa/sasa\"\n}","accessValues":"{\n\t\"Host\": \"test\",\n\t\"Port\": \"3389\",\n\t\"User\": \"admin\",\n\t\"Password\": \"admin\"\n}"}
//{"name":"Another test","dataTypeUid":"dd-002-002","dataTypeName":"AVI","dataTypeVersion":"2.0","multiplicity":1,"dataStructureUid":null,"dataStructureName":null,"dataStructureVersion":null,"accessTypeUid":"dd-007-000","accessTypeName":"FileUpload","accessTypeVersion":"1.0","values":"{\n\t\"LocalPath\": \"sas\"\n}","accessValues":"{}"}
@Serializable
data class DatasetCreate(
    val name: String,
    val multiplicity: DatasetMultiplicity,
    val dataTypeUid: String,
    val dataTypeName: String,
    val dataTypeVersion: String,
    val dataStructureUid: String?,
    val dataStructureName: String?,
    val dataStructureVersion: String?,
    val accessTypeUid: String,
    val accessTypeName: String,
    val accessTypeVersion: String,
    val values: String,
    val accessValues: String
)
