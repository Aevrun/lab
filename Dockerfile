FROM ubuntu:22.04

ARG UUID=1000
ARG GUID=1000
ENV USERNAME=lab

RUN \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
        ca-certificates-java \
        curl \
        graphviz \
        openjdk-8-jre-headless \
        python3-distutils \
        gcc \
        python3-dev \
        && \
    rm -rf /var/lib/apt/lists/*

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
RUN python3 get-pip.py

COPY requirements.txt requirements.txt

RUN  echo "Installing Python packages..." && \
    pip install --break-system-packages -r requirements.txt

RUN jupyter contrib nbextension install --system

RUN jupyter nbextension enable splitcell/splitcell --system

ENV SCALA_VERSION=2.13.10
ENV ALMOND_VERSION=0.13.14

RUN \
    curl -L -o coursier https://git.io/coursier-cli && \
    chmod +x coursier && \
    ./coursier \
        bootstrap \
        -r jitpack \
        sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
        --sources \
        --default=true \
        -o almond && \
    ./almond --install --global && \
    \rm -rf almond couriser /root/.cache/coursier

RUN groupadd -g $GUID $USERNAME && \
    useradd -m -u $UUID -g $GUID -s /bin/bash $USERNAME
USER $USERNAME

RUN mkdir -p /home/${USERNAME}/.jupyter/nbconfig/
RUN echo '{"nbext_hide_incompat": false}' > /home/${USERNAME}/.jupyter/nbconfig/common.json

EXPOSE 8888

WORKDIR /labs

ENTRYPOINT jupyter notebook --no-browser --ip 0.0.0.0 --port 8888  --NotebookApp.token='' --NotebookApp.password=''