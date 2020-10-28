# Pocket To Kindle

Web scrape Pocket articles, export to single pdf and send to kindle.

Only article type Pocket items supported.

### How to run
1. Populate application.properties
```
# pocket
pocket.url=https://getpocket.com/
pocket.username=
pocket.password=

# email
mail.host=
mail.port=
mail.sslenable=
mail.auth=true
mail.from=
# use app password and not normal password if using gmail smtp
mail.password=
mail.to=
```
2. Run main class `PocketToKindleApplication`
3. Trigger the pocket to kindle workflow from swagger-ui : http://localhost:8072/swagger-ui/