# 03 · Markdown

Level: junior. Status: not started.

## Change

The body of a note is written in Markdown and shown as formatted HTML; the stored text does not change.

## Why

In 02 a note is plain text: lists, links and headings are shown as raw characters.

## Tools

- A CommonMark implementation for Java, chosen when the stage starts; the rendered HTML is sanitised before Thymeleaf writes it unescaped.

## Proof

- The page tests pass.
- Markdown is rendered, and script tags in a note are not executed.
