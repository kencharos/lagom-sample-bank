lagom sample application
-------------------------

## abstraction

This is lagom sample bank account application for my study.

## API

### create bank account

+ POST /api/account
+ BODY `{"id":""<account id>"", "name":"<account name>"}`

### deposit money

+ PUT /api/account/:account_id/deposit
+ BODY `{"amount":<deposit MONEY>}`

### withdraw money

+ PUT /api/account/:account_id/withdrawal
+ BODY `{"amount":<withdrawal monay>}`

### get account information
+ GET /api/account/:account_id


### list account transaction history
+ GET /api/account/:account_id/history

## License

[Apache 2 license](http://www.apache.org/licenses/LICENSE-2.0)
