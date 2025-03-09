# Contributing to Our Project

Thank you for your interest in contributing to our project! Please follow the guidelines below to ensure a smooth contribution process.

## 🚀 Getting Started

### 1️⃣ Clone the Repository
1. Go to our GitHub repository.
2. Click on the green **Code** button.
3. Copy the HTTPS link.
4. Open your terminal and navigate to the directory where you want to clone the project.
5. Run the following command:
   ```sh
   git clone <copied-link>
   ```

### 2️⃣ Switch to the Development Branch
Once the repository is cloned, switch to the `dev` branch:
   ```sh
   git checkout dev
   ```

### 3️⃣ Run Unit Tests
Before making any modifications, ensure that the project is correctly set up by running:
   ```sh
   mvn clean test
   ```
This command:
- Cleans the project by removing previously compiled files.
- Runs all unit tests to verify the integrity of the codebase.

## 📝 Working on an Issue

### 1️⃣ Create or Find an Issue
- Check the **Issues** tab in the GitHub repository.
- If your issue already exists, proceed to create a branch for it.
- Otherwise, create a new issue describing the problem or feature request.

### 2️⃣ Create a Branch for Your Issue
- Inside the issue, click on **Create a Branch**.
- Ensure that the new branch is created based on `dev`, not `main`.
- Alternatively, you can manually create a branch using:
   ```sh
   git checkout -b <branch-name> dev
   ```

### 3️⃣ Access Your Branch in the Code Editor
- Open your code editor (e.g., VS Code, IntelliJ).
- Make sure your current branch matches the one created for the issue.
- You can check your current branch using:
   ```sh
   git branch
   ```
- If necessary, switch to your branch using:
   ```sh
   git checkout <branch-name>
   ```

### 4️⃣ Sync Your Branch with `dev`
To ensure your branch has the latest changes from `dev`, run:
   ```sh
   git pull origin dev
   ```

## 🔨 Implementing Changes
Once your branch is set up, you can start working on your issue and making changes.

### 1️⃣ Remove Cached Files
Before committing, run:
   ```sh
   git rm -r --cached target/
   ```
This prevents unnecessary compiled files from being pushed to the repository.

### 2️⃣ Commit Your Changes
After making modifications, commit them with a meaningful message:
   ```sh
   git commit -m "Your commit message here"
   ```

### 3️⃣ Push Your Changes
Push your updates to the remote repository:
   ```sh
   git push origin <branch-name>
   ```

## 📩 Create a Pull Request
1. Go to the GitHub repository.
2. Open the **Pull Requests** tab.
3. Click **New Pull Request**.
4. Select your branch and compare it with `dev`.
5. Choose at least one contributor to review your changes.
6. Submit the pull request.

🎉 **Congratulations! You've successfully contributed to the project!**

---
If you have any questions, feel free to ask in the discussion section or contact the maintainers. Happy coding! 🚀

