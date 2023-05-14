# Autistic Disorder classification using Logistic Regression

This is a command-line tool for training, testing, and predicting Autism based on a set of given parameters.

## Usage

To use this program, provide one of the following arguments:

- `train`: Train the logistic regression model using the provided training dataset.
- `test`: Test the pre-trained model with a testing dataset.
- `predict`: Use the model to predict the possibility of autism based on the user's responses to a set of questions.

## Data

The model is trained on the dataset: [Autism Screening Adult Data Set](https://code.datasciencedojo.com/datasciencedojo/datasets/tree/master/Autism%20Screening%20Adult)

Questions used for the survey: [Autism Spectrum Quotient (AQ)](https://docs.autismresearchcentre.com/tests/AQ10.pdf)

## Program arguments discription

### Train mode

In 'train' mode, the program will train a logistic regression model using the provided training dataset. The trained model's weights are then saved for future use.

### Test mode

In 'test' mode, the program loads the pre-trained model and test it against a provided testing dataset. It will output various metrics like accuracy, precision, recall, and F1 score.

### Predict mode

In 'predict' mode, the program will interactively ask the user a series of questions. Based on the user's responses, it will use the pre-trained model to predict the likelihood of the user having autism.

Note: The 'predict' mode is not a definitive diagnostic tool. It only gives a probability based on the user's responses. Please consult with a healthcare professional for a comprehensive evaluation.
