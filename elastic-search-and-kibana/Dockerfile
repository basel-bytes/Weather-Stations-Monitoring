FROM python:3.8

# Set the working directory in the container
WORKDIR /elasticsearch

# Copy the script file into the container
COPY uploadToelastic.py .

# Install the required dependencies
RUN pip install elasticsearch pyarrow confluent-kafka

# Run the script when the container starts
CMD ["python", "uploadToelastic.py"]