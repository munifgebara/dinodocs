
@startuml
<#list annotadeds as _anotacao, classes>
<#assign anotacao = _anotacao?remove_beginning("interface ")?replace(".", "_")>
package "${anotacao}" {
   <#list classes as _classe>
   <#assign classe = _classe?remove_beginning("class ")?remove_beginning("interface ")?replace(".", "_")>
   class ${classe} {
     -String id
     -String name
     +String name()
   }
   </#list>
}
  </#list>
@enduml

