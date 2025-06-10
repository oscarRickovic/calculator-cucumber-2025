[![CI](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/ci.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/ci.yml)
[![CodeQL](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/codeql.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/codeql.yml)
[![Dependency Review](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/dependency-review.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/dependency-review.yml)
[![Maven Build](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/maven.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/maven.yml)
[![PMD Analysis](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/pmd.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/pmd.yml)
[![Release](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/release.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/release.yml)
[![Scorecard](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/scorecard.yml/badge.svg)](https://github.com/oscarRickovic/calculator-cucumber-2025/actions/workflows/scorecard.yml)
# Contributing to Calculator Cucumber Project

## 🎉 Welcome Contributors!

Thank you for your interest in contributing to the **Calculator Cucumber** project! We value and appreciate all contributions, whether it's fixing bugs, adding features, improving documentation, or enhancing test coverage.

## 📋 Project Overview

This project is a calculator application using:
- Java 21
- Maven
- JUnit 5
- Cucumber for Behavior-Driven Development (BDD)
- JavaFX for the user interface

## 🛠 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** Development Kit (JDK)
- **Maven** 3.8+ 
- **Git**
- An Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse

## 🚀 Setting Up the Project

### 1. Clone Your Forked Repository

```bash
git clone https://github.com/oscarRickovic/calculator-cucumber-2025.git
cd calculator-cucumber-2025
```

### 3. Build and Verify the Project

```bash
# Clean and install dependencies
mvn clean install

# Run tests
mvn test

# Run the application FX
mvn javafx:run

# Run the Spring boot application
mvn spring-boot:run

# Run the command Line Console.
mvn exec:java
```

## 🤝 Contribution Workflow

### 1. Create a New Branch

- First create new issue, or get an issue from issues bar.
- Assign the issue to your self, you can assign someone else with you if you want to ofcrs.
- Add the issue description in the Project page.
- Create new branch based on "DEV" branch (not master).

```bash
# Create a new branch for your contribution
git checkout -b feature/your-feature-name
# Or for a bugfix
git checkout -b bugfix/issue-description
```

### 2. Make Your Changes

- Follow Java coding conventions
- Write clear, concise, and meaningful code
- Add/update unit and integration tests
- Ensure all tests pass before committing

### 3. Commit Your Changes

Use meaningful commit messages following conventional commit format:

```bash
# Examples:
git commit -m "feat: add advanced mathematical operations"
git commit -m "fix: resolve division by zero error"
git commit -m "docs: update README with new installation instructions"
```

Commit types:
- `feat:` - New features
- `fix:` - Bug fixes
- `docs:` - Documentation updates
- `test:` - Test-related changes
- `refactor:` - Code restructuring
- `perf:` - Performance improvements

### 4. Push Your Changes

```bash
git push origin feature/your-feature-name
```

### 5. Create a Pull Request

1. Click "New Pull Request"
2. Select the base repository's `dev` branch
3. Provide a clear title and description of your changes
4. Submit the pull request
5. wait until the Pull request was approved
6. close the issue.
7. make the task `DONE` in project board.

## 🧪 Testing Guidelines

- Write unit tests for new functionality using JUnit 5
- Use Cucumber for behavior-driven tests
- Aim for high test coverage
- Ensure all tests pass: `mvn test`

## 📋 Code Style and Quality

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Keep methods small and focused
- Add JavaDoc comments for public methods
- Run `mvn checkstyle:check` to verify code style

## 🐛 Reporting Issues

1. Check existing issues to avoid duplicates
2. Use GitHub Issues to report:
   - Bugs
   - Feature requests
   - Performance issues
3. Provide:
   - Clear description
   - Steps to reproduce
   - Expected vs. actual behavior
   - Screenshots (if applicable)

## 📚 Documentation

- Update README.md for any significant changes
- Add/update inline code comments
- Keep documentation clear and concise

## 🤔 Need Help?

- Open an issue with questions
- Use GitHub Discussions
- Check existing documentation

## 📜 Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Collaborate and support each other

## 🏆 Thanks!

Your contributions make this project better. We appreciate your time and effort!

### Contributors Hall of Fame

(Maintainers can add a list of contributors here)

---

**Happy Coding! SE7 🚀✨**

@ Kenza Khemar.
@ Abdelhadi Agourzam
@ Mohssine 
@ Mohamed Elismaiyly.

