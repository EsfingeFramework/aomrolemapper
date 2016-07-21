# AOMCouchPersistenceFramework

Componente de persistência para ser utilizado em conjunto com o Framework
Esfinge AOM Role Mapper.
Provê a persistência dos dados e metadados do AOM Role Mapper em bancos
de dados CouchDB.

## Utilização

Inclua o JAR do AOMCouchPersistenceFramework no ClassPath da aplicação
que faz uso do AOMRoleMapper. O componente será carregado por meio do
Service Locator do Java.

### Configuração

No mesmo diretório onde o arquivo JAR foi copiado crie um diretório 
com o nome *Config* e dentro dele um arquivo chamado *CouchAOMConfiguration.json*
este arquivo, no formato JSON, contém as configurações para acesso
ao banco de dados CouchDB.

**Config/CouchAOMConfiguration.json**
```json
{
	"dbPrefix": "aomdb",
	"host": "localhost",
	"port": 5984,
	"protocol": "http",
	"createDbIfNotExist": true,
	"username": "",
	"password": "",
	"path": ""
}
```
Todos os parâmetros mostrados no exemplo acima devem estar presentes no arquivo de configuração.

* __dbPrefix__ - Prefixo para os bancos de dados utilizados, o AOMRoleMapper
utiliza 2 bancos de dados com os nomes _[dbPrefix]-entity_ e _[dbPrefix]-entity_type_.
* __host__ - Nome ou IP do computador onde o banco de dados está instalado.
* __port__ - Porta na qual o CouchDB pode ser acessado.
* __protocol__ - Protocolo de comunicação utilizado _http_ ou _https_.
* __createDbIfNotExist__ - Criar banco de dados automaticamente caso não existam.
* __username__ - Nome de usuário (se deixado em branco o acesso será feito sem login).
* __password__ - Senha (pode ser deixada em branco).
* __path__ - Prefixo para a URL do banco de dados (pode ser deixado em branco).

## Limitações e Desenvolvimento Futuro

- EntityTypes e Entities em bancos de dados separados.

As EntityTypes e Entities são armazenadas em bancos de dados separados.
Isso é feito pois o CouchDB não possui coleções e assim no caso de um
único banco de dados poderia haver conflitos entre os IDs utilizados para
Entities e EntityTypes.

- IDs da Entities devem ser do tipo String.

As chaves utilizadas para identificação nos documentos CouchDB são obrigatoriamente
armazenadas como Strings, dessa forma todas as Entities armazenadas devem
possuir o ID deste tipo.

## Referências

* [Esfinge AOM Role Mapper](esfinge.sourceforge.net/AOM.html)
* [Apache CouchDB](http://couchdb.apache.org/)
