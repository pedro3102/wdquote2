class EventBus {
  events: object;

  constructor() {
    this.events = {};
  }

  checkSubscription(eventName: string, fn: Function): number {
    return this.events[eventName].findIndex(subs => subs.toString() === fn.toString());
  }

  on(eventName: string, fn: Function): void {
    this.events[eventName] = this.events[eventName] || [];
    const pos = this.checkSubscription(eventName, fn);
    if (pos === -1) {
      this.events[eventName].push(fn);
    } else {
      this.events[eventName][pos] = fn;
    }
  }

  off(eventName: string, fn: Function): void {
    if (this.events[eventName]) {
      for (let i = 0; i < this.events[eventName].length; i += 1) {
        if (this.events[eventName][i] === fn) {
          this.events[eventName].splice(i, 1);
          break;
        }
      }
    }
  }

  emit(eventName: string, data: any): void {
    if (this.events[eventName]) {
      this.events[eventName].forEach(fn => {
        fn(data);
      });
    }
  }
}

export const eventBus = new EventBus();

export default eventBus;
