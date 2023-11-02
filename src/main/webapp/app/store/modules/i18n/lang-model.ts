export interface ILanguage {
  name?: string
  key?: string
  label?: string
  title?: string
  icon?: string
}

export class Language implements ILanguage {
  constructor(public name?: string, public key?: string, public title?: string, public icon?: string) {}
}
