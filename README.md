# CLOSHA2.0

> Recent advances in various computational algorithms for high-throughput sequencing technologies have accelerated the progress toward a comprehensive
> understanding of genetic information. However, most of these tools are not user-friendly and interoperable, making it difficult for biologists to design
> pipelines to meet their needs. We present CLOSHA2.0, a bioinformatics analysis workflow management standard software. Using pipelines provided by CLOSHA2.0,
> even scientists with a little computer programming experience can easily analyze NGS data. CLOSHA2.0 can also support a computational environment to share
> analysis workflows and results with other research scientists.

## Installation

Closha 2.0 Workbench does not require a traditional installation and can be used by building the Java package. To enhance portability and convenience, we have packaged the Java runtime environment with the application into a portable program, providing executables for Linux, Windows, and Mac OSX. This installer does not require administrator privileges and should be run with standard user permissions. Please note that the Windows and MacOS installers are unsigned, so you may need to confirm a security message during installation.

### Get binary installer

Check out [our releases page](https://github.com/kobic-dev/closha/releases) for Windows, Linux and MacOS executables.

### Execution

Run the downloaded file.
The installer does not require administrator privileges and should be run with regular user permissions.

### Important Note

The Windows and MacOS installers are not signed. A security warning may appear during installation. Please confirm the message to proceed with the installation.

### Installation Completion

Once the installation is complete, you can start using Closha 2.0 Workbench.

### Additional Information
* Supported Environment: Java 11 or higher is required. You can use the Java runtime environment included with the installer.
* Updates: When a new version is released, download the latest installer from the GitHub Releases page and follow the installation instructions.
* Troubleshooting: If you encounter any issues during installation, please report them on the [Issues page](https://github.com/kobic-dev/closha/issues).

## Features
Closha 2.0 is designed to enable researchers of all technical backgrounds to perform complex genomic analyses with ease and efficiency. We prioritize user accessibility by offering an intuitive interface and convenient services.

* User-Friendly Interface: Intuitive design allows users to perform analyses without requiring extensive technical knowledge.
* Scalable Cloud-Based Architecture: Leverages cloud computing to ensure high performance and scalability for large datasets.
* Integrated Workflow Management: Offers curated analysis pipelines and customizable workflows to meet diverse research needs.
* Multi-Platform Support: Available for Linux, Windows, and Mac OSX, with installers that do not require administrator privileges.

## Tutorial
Please consult the [tutorial](https://kobic.re.kr/closha2/howtouse) for a detailed step-by-step guide on how to use Closha 2.0.

## Scrrenshot
<p align="center">
<img width="969" alt="image" src="https://github.com/user-attachments/assets/11bffd04-1f09-41a3-902d-2cee15b20b36">
</p>

## System Architecture
<p align="center">
<img width="969" alt="image" src="https://github.com/user-attachments/assets/e9ef8864-a1ac-4827-9026-1e2f5cd57ef3">
</p>
Composition of HPC-Cloud-Based CLOSHA2.0 System. Previous constructed system has difficult in analyzing massive dielectric date, CLOSHA2.0 system constructed system that enables you to the massive dielectric date analysis. Even though it is complicated and various conditions, it has tried to demonstrate optimum performance through user install CLOSHA2.0　program and organic connection of workbench, it obtains contents of analysis service workflow and tries to offer various analysis workflow to numerous researchers.
</br>

</br>
<p align="center">
<img width="969" alt="image" src="https://github.com/user-attachments/assets/161cef0b-ab03-4e9a-80ca-196cb5d55eda">
</p>
Closha 2.0 supports container-based analysis execution, allowing analytical tools to run independently within containers. This prevents dependency issues and conflicts between different tools. As a result, multiple versions of the same tool can be executed in the same environment, maintaining the independence of analytical tool execution. Additionally, containers provide a consistent execution environment, ensuring the reproducibility of analysis tasks. The container-based architecture is easily scalable in a cluster environment, making it suitable for large-scale data processing. This enhances both scalability and reproducibility. Containers can run on various cloud platforms and on-premise environments, increasing the compatibility and portability of the analysis pipeline. This flexibility allows users to perform analysis tasks without being tied to a specific infrastructure.

These features significantly contribute to Closha 2.0 evolving into a more efficient and stable analysis platform.

## Contact

If you have questions or encounter any issues, we invite you to open an [issue on our GitHub repository](https://github.com/kobic-dev/closha/issues). This allows other users to collaborate and (hopefully) provide timely answers to your questions. If your request contains confidential information or is not suitable for a public issue, please send us an email at [cloud_team@kobic.kr](cloud_team@kobic.kr).
