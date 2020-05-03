export interface IHome {
  id?: number;
  firstName?: string;
  lastName?: string;
  adress?: string;
  phone?: string;
  locality?: string;
}

export class Home implements IHome {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public adress?: string,
    public phone?: string,
    public locality?: string
  ) {}
}
