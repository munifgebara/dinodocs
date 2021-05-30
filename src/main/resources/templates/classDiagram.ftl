
@startuml
<#list entityPackages as package, entities>
package "${package}" {
   <#list entities as entity>
   class ${entity.simpleName} {
     <#list entity.instanceFields as field>
       -${field.type.simpleName} ${field.name}
     </#list>
     }
   </#list>
}
</#list>
<#list entityPackages as package, entities>
   <#list entities as entity>
     <#list entity.toOneAssociationFields as toOne>
         ${entity.type.simpleName} <|-- ${toOne.simpleName}
     </#list>


   </#list>
</#list>




@enduml

