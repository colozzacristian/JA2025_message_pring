---
title: Email service

---

# Email service
## Index
- [Send email](###send_email)
## Requests

## APIs
All these APIs are only to be used by the other microservice of these application.
### send_email
#### Endpoint
- type `POST`
- `/send/email`
#### Input
```json
{
    dest:"email"
    subject: "subject"
    body: "htmlBody"
}
```

#### Returns

##### Valid
- 200 HTTP response code.
##### Invalid
- 400 HTTP response code.


<!--
### send_sms

#### ENDPOINT
- type `POST`
- `/send/sms`
#### Input
```json
{
    dest:"number"
    body: "plainTextBody"
}
```

#### Returns

##### Valid
- 200 HTTP response code.
##### Invalid
- 400 HTTP response code.



### send_telegram

#### ENDPOINT
- type `POST`
- `/send/telegram`
#### Input
```json
{
    dest:"number"
    body: "markdownBody"
}
```

#### Returns

##### Valid
- 200 HTTP response code.
##### Invalid
- 400 HTTP response code.


-->
