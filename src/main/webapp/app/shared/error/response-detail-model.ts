export interface IErrorDetails {
  error: string;
  errorDescription: string;
}

export class ErrorDetails implements IErrorDetails {
  constructor(public error: string, public errorDescription: string) {}
}
