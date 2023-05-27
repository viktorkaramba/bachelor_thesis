import {User} from "./user";

export class LoginResponse {
  accessToken: string="";
  refreshToken: string="";
  user: User = new User();
}
