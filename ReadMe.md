# Secret Santa Email Sender Tool
This little Java application draws the Santa (donor) and the giftee and sends the Santa/donor an email with the giftee's name.

Features:
* Create your own mail template
* Configure the participants, the email addresses and optionally exclusion rules
* Simulation mode
* Doesn't store/log the secret drawing (so the operator can participate without unfair knowledge)

# Manual
## Preparation
* Checkout the project and open/import it in your favorite Java IDE.
  Because you use this once a year, you may skip generating an executable JAR file...
* Create and configure the application.properties
* Create and configure the rules.json (participants configuration)
* Create and configure the mailtemplate.html (mail template)

## Run
Just run the _main_ in _secretsanta.Startup_. Maybe you want to run some try runs / simulations first

## Configuration
### application.properties
```properties
spring.mail.username=the-sender-email-account@example.nowhere
spring.mail.password=the-sender-email-account-password
spring.mail.host=the smtp server of the mail provider
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

secretsanta.sendMail=true
secretsanta.logSecret=false
secretsanta.logSecretMail=false
secretsanta.mailSubject=The subject of the Secret Santa mails
secretsanta.mailBanner=

secretsanta.mailtemplate=mailtemplate.html
secretsanta.rules=rules.json
```
* For try runs / simulations, set `secretsanta.sendMail=false`, to avoid sending an email.
  * You can directly log the drawn Santa and giftee with  `secretsanta.logSecret=true` (one line per relation).
  * You can log the complete email using `secretsanta.logSecretMail=true`
  * You can include an optional banner in your mailtemplate like `"<h1 style=\"color: red\">THIS IS A TEST MAIL, IGNORE IT!</h1>"` using `secretsanta.mailBanner=`.

### rules.json
```json
{
    "santas": [
        {"name": "Grandpa", "email": "gradnma@example.void"},
        {"name": "Grandma", "email": "grandpa@example.void"},
        {"name": "Mom", "email": "mom@example.void"},
        {"name": "Dad", "email": "dad@example.void"},
        {"name": "Mike", "email": "mike@nowhere.void"},
        {"name": "Michelle", "email": "sweet-cherry@nowhere.void"},
        {"name": "John", "email": "johny3@nowhere.void"}
    ]
}
```
Sometimes you have restriction like couples not having themselves as Santa. Optionally, you can define some _exclusion groups_:
```json
{
    "santas": [
        {"name": "Grandpa", "email": "gradnma@example.void"},
        {"name": "Grandma", "email": "grandpa@example.void"},
        {"name": "Mom", "email": "mom@example.void"},
        {"name": "Dad", "email": "dad@example.void"},
        {"name": "Mike", "email": "mike@nowhere.void"},
        {"name": "Michelle", "email": "sweet-cherry@nowhere.void"},
        {"name": "John", "email": "johny3@nowhere.void"}
    ],
	"exclusions": [
		["Grandpa", "Grandma"],
		["Dad", "Mom"],
		["Mike", "Michelle"]
	]
}
```

### mailtemplate.html
Create you our own mail template. The following example contains all supported placeholders:
```html
${banner}
<p>Hello ${santaFrom}</p>

<p>
    This is your personal Secret Santa instruction for Christmas ${currentYear}.<br>
    You gift <b>${santaTo}</b>!
</p>

See you soon...
${banner}
```
All the placeholders are optional.

Extra Tip: If you want to send all participants a first introducing mail first (to check all email addresses are correct and they really receive the mails),
just use a template without including the `${santaTo}`:
```html
${banner}
<p>Hello ${santaFrom}</p>

<p>Our Secret Santa drawing starts soon. Please send me a text message to confirm to received this mail.</p>

Thanks,<br>
Mike
```