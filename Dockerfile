 
FROM ubuntu:20.04
RUN apt-get update
RUN apt-get -y install curl unzip zip
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java && \ 
    yes | sdk install kotlin"

RUN bash -c "curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.44.0/ktlint && \
             chmod a+x ktlint && \
             mv ktlint /usr/local/bin/"
