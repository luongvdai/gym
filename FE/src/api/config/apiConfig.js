var ApiConfig = {
  development: "http://localhost:5000",
  production: "https://fpt-gym-management.azurewebsites.net",
};

export default ApiConfig[process.env.NODE_ENV];
