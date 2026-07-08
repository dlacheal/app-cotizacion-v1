export const getApiBaseUrl = (): string => {
  if (typeof window !== 'undefined' && window.location) {
    return `http://${window.location.hostname}:8081/api/v1`;
  }
  return 'http://localhost:8081/api/v1';
};
