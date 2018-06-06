# Routes

## No auth
* POST /api/users/login - LoginRequest => Option[User]
* POST /api/users/register - RegistrationRequest => Option[User]

## Auth
* GET /api/user - * => User
* PUT /api/user - UpdateUserRequest => Option[User]
