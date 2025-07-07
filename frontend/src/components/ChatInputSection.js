import React from 'react';
import { Row, Col, Container, Card } from 'react-bootstrap';
import BotAttachFile from './BotAttachFile';
import BotTextArea from './BotTextArea';
import SendButton from './SendButton';

export default function ChatInputSection({ message, setMessage, file, setFile }) {
  return (
    <Container className="my-4">
      <Card className="p-4 shadow-lg border-0 rounded">
        <Row className="gy-3 align-items-center">
          <Col md={4}>
            <BotAttachFile file={file} setFile={setFile} />
          </Col>
          <Col md={6}>
            <BotTextArea message={message} setMessage={setMessage} />
          </Col>
          <Col md={2}>
            <SendButton message={message} file={file} />
          </Col>
        </Row>
      </Card>
    </Container>
  );
}
