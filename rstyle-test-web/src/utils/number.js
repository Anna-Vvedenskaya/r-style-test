export function toFixed(s, fractionDigits) {
  return Number.parseFloat(s).toFixed(fractionDigits);
}

export function convertSum(s) {
  return toFixed(s / 100, 2);
}