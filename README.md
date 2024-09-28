# Daraja API Integration

This project is a **Spring Boot** application designed to integrate with Safaricom's **Daraja API** for M-Pesa services. The application implements various M-Pesa services such as C2B (Customer to Business) transactions, B2C (Business to Customer) payments, STK Push, checking account balance, and transaction status queries.

## Features

- **Access Token Management**: Fetches and manages OAuth 2.0 tokens for authenticating M-Pesa API requests.
- **Register URLs**: Registers confirmation and validation URLs for M-Pesa C2B transactions.
- **Simulate C2B Transactions**: Simulates M-Pesa transactions for testing purposes.
- **Perform B2C Transactions**: Processes business-to-customer payments.
- **Transaction Status Query**: Fetches the status of M-Pesa transactions.
- **STK Push**: Initiates an STK push for payments via M-Pesa.
- **Check Account Balance**: Queries the account balance for the business shortcode.

## Technologies Used

- **Spring Boot**: The core framework for building the REST API.
- **OkHttp**: A client for making HTTP requests to the M-Pesa Daraja API.
- **Jackson**: For parsing JSON responses.
- **Lombok**: To reduce boilerplate code (e.g., for logging).
- **M-Pesa Daraja API**: Safaricom’s M-Pesa API for mobile money services.
- **Maven**: For managing dependencies and building the project.

## Prerequisites

1. **Java 21** or higher installed.
2. **Maven** installed.
3. **M-Pesa Daraja API account** with valid credentials, including:
   - Consumer Key
   - Consumer Secret
   - Shortcode
   - B2C Initiator Name and Password
   - Passkey for STK Push

## Project Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/felixojiambo/MpesaBridge.git
   cd MpesaBridge
   ```

2. Configure the application by updating the `MpesaConfig` with your M-Pesa Daraja API credentials:

   ```yaml
   mpesa:
     consumerKey: YOUR_CONSUMER_KEY
     consumerSecret: YOUR_CONSUMER_SECRET
     shortCode: YOUR_SHORT_CODE
     b2cInitiatorName: YOUR_INITIATOR_NAME
     b2cInitiatorPassword: YOUR_INITIATOR_PASSWORD
     stkPushShortCode: YOUR_STK_SHORT_CODE
     stkPassKey: YOUR_STK_PUSH_PASSKEY
     oauthEndpoint: https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials
     registerUrlEndpoint: https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl
     simulateTransactionEndpoint: https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate
     b2cTransactionEndpoint: https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest
     transactionResultUrl: https://sandbox.safaricom.co.ke/mpesa/transactionstatus/v1/query
     checkAccountBalanceUrl: https://sandbox.safaricom.co.ke/mpesa/accountbalance/v1/query
     stkPushRequestUrl: https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest
     lnmQueryRequestUrl: https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query
   ```

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication

- **Get Access Token**: Automatically managed by the service.

### C2B (Customer to Business) Simulation

- **URL**: `/mobile-money/c2b/simulate`
- **Method**: POST
- **Description**: Simulates a C2B transaction.
- **Request Body**:
  ```json
  {
    "shortCode": "YOUR_SHORT_CODE",
    "commandID": "CustomerPayBillOnline",
    "amount": 1000,
    "msisdn": "254708374149",
    "billRefNumber": "12345678"
  }
  ```

### B2C (Business to Customer) Payments

- **URL**: `/mobile-money/b2c/payment`
- **Method**: POST
- **Description**: Sends payments from the business to a customer.
- **Request Body**:
  ```json
  {
    "amount": 1000,
    "commandID": "BusinessPayment",
    "partyB": "254708374149",
    "remarks": "Payment",
    "occassion": "Occasion"
  }
  ```

### STK Push

- **URL**: `/mobile-money/stkpush`
- **Method**: POST
- **Description**: Initiates an STK push request for mobile payment.
- **Request Body**:
  ```json
  {
    "amount": 1000,
    "phoneNumber": "254708374149"
  }
  ```

### Transaction Status Query

- **URL**: `/mobile-money/transactionstatus`
- **Method**: POST
- **Description**: Queries the status of a transaction.
- **Request Body**:
  ```json
  {
    "transactionID": "ABC123DEF456"
  }
  ```

### Check Account Balance

- **URL**: `/mobile-money/accountbalance`
- **Method**: POST
- **Description**: Fetches the business account balance.

## Helper Utilities

- **HelperUtility**: Utility class for encoding data and generating timestamps.
- **Constants**: Contains static values for HTTP headers, M-Pesa command IDs, and identifiers.

## Running Tests

To run unit tests:

```bash
mvn test
```

## Logging

The application uses Lombok’s `@Slf4j` annotation for logging various events such as transaction failures, access token issues, and request/response details.

## Contributing

Feel free to fork the repository and submit pull requests for any bug fixes or feature enhancements.

## License

This project is licensed under the MIT License.
