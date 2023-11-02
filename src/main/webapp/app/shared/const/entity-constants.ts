export enum ActionType {
  DEFAULT = 'default',
  LINK = 'link',
  BUTTON = 'button',
  DIVIDER = 'divider',
  EMITTER = 'emitter',
  DROPDOWN = 'dropdown',
}

export enum RenderType {
  BOOLEAN = 'boolean',
}

export enum SortModeType {
  MULTIPLE = 'multiple',
  SINGLE = 'single',
}

export enum PrimeSeverities {
  SUCCESS = 'success',
  DANGER = 'danger',
  WARNING = 'warning',
  INFO = 'info',
}

export enum ActionTarget {
  BLANK = '_blank',
  SELF = '_self',
  PARENT = '_parent',
  TOP = '_top',
  FRAMENAME = 'framename',
}

export enum BasicAction {
  ADD = 'add',
  DETAILS = 'details',
  DELETE = 'delete',
  CONFIRM = 'confirm',
}

export enum DataType {
  TEXT = 'text',
  DATE = 'date',
  SELECT = 'text',
  NUMERIC = 'numeric',
  BOOLEAN = 'boolean',
}

export enum FilterType {
  TEXT = 'text',
  DATE = 'date',
  SELECT = 'select',
  MULTISELECT = 'multi-select',
  NUMERIC = 'numeric',
  BOOLEAN = 'boolean',
}

export enum EditRowType {
  TEXT = 'text',
  DATE = 'date',
  SELECT = 'select',
  MULTISELECT = 'multi-select',
  NUMERIC = 'numeric',
  BOOLEAN = 'boolean',
}

export enum FilterDisplayType {
  MENU = 'menu',
  ROW = 'row',
}

export enum FilterMatchMode {
  STARTS_WITH = 'startsWith',
  CONTAINS = 'contains',
  NOT_CONTAINS = 'notContains',
  ENDS_WITH = 'endWith',
  EQUALS = 'equals',
  NOT_EQUALS = 'notEquals',
  IN = 'in',
  SPECIFIED = 'specified',
  LESS_THAN = 'lessThan',
  LESS_THAN_OR_EQUAL_TO = 'lessThanOrEqualTo',
  GREATER_THAN = 'greaterThan',
  GREATER_THAN_OR_EQUAL_TO = 'greaterThanOrEqualTo',
  BETWEEN = 'between',
  DATE_IS = 'dateIs',
  DATE_IS_NOT = 'dateIsNot',
  DATE_BEFORE = 'dateBefore',
  DATE_AFTER = 'dateAfter',
}

export enum FilterOperatorOptions {
  AND = 'and',
  OR = 'or',
}

export enum SeverityCodes {
  PRIMARY = '#2563eb',
  SECONDARY = '#64748B',
  SUCCESS = '#22C55E',
  INFO = '#22C55E',
  WARNING = '#F59E0B',
  HELP = '#A855F7',
  DANGER = '#EF4444',
}

export enum LocationType {
  WAREHOUSE = 'WAREHOUSE',
  WIP = 'WIP',
  ON_PAINT = 'ON_PAINT',
}

export enum DatatableSize {
  SMALL = 'sm',
  NORMAL = 'normal',
  LARGE = 'lg',
}

export enum TextAlign {
  END = 'end',
  CENTER = 'center',
  START = 'start',
}

export enum OperationType {
  IN = 'IN',
  OUT = 'OUT',
}

export enum OperationCounterpart {
  NONE = 'NONE',
  VENDOR = 'VENDOR',
  LOCATION = 'LOCATION',
  CUSTOMER = 'CUSTOMER',
}

export enum StockStatus {
  IN_STOCK = 'IN_STOCK',
  LOW_STOCK = 'LOW_STOCK',
  OUT_STOCK = 'OUT_STOCK',
}

export enum ScrollHeight {
  FLEX = 'flex',
  SM = '400px',
  MD = '600px',
  LG = '800px',
}

export enum MovementStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  CANCELED = 'CANCELED',
}

export enum EditingStatus {
  NONE = 'none',
  SAVING = 'saving',
  SAVED = 'saved',
  ERROR = 'error',
}
