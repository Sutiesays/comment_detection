from diagrams import Diagram, Cluster, Edge
from diagrams.generic.compute import Server
from diagrams.generic.device import Mobile
from diagrams.generic.storage import Storage
from diagrams.programming.language import Python
from diagrams.hardware.computer import Computer
from diagrams.electronics.power import UPS
from diagrams.electronics.cable import Cable
from diagrams.aws.compute import EC2
from diagrams.generic.office import Drawing

def create_test_system_diagram():
    with Diagram("多模块集成自动测试系统硬件结构", show=False, direction="TB"):

        # 供电单元
        with Cluster("供电单元"):
            ups = UPS("UPS不间断电源")
            ac_dc = Drawing("交直流电源")
            power_supply = [ups, ac_dc]

        # 主控单元
        with Cluster("主控单元"):
            main_computer = Computer("主控计算机")
            can_card = Storage("CAN接口卡")
            mlvds_card = Storage("MLVDS接口卡")
            kvm = Mobile("KVM切换器")
            programmer = Storage("FPGA/CAN烧写器")

            main_computer >> can_card
            main_computer >> mlvds_card
            main_computer >> kvm
            main_computer >> programmer

        # 测试仪器集群
        with Cluster("测试仪器集群"):
            signal_gen = Server("信号源")
            spectrum = Storage("频谱分析仪")
            oscilloscope = Computer("示波器")
            vna = Server("矢量网络分析仪")
            load = UPS("电子负载")
            instruments = [signal_gen, spectrum, oscilloscope, vna, load]

        # 检测单元
        with Cluster("检测单元"):
            fixture = Cable("专用测试夹具")
            power_module = EC2("电源模块")
            switch_module = EC2("开关矩阵模块")

            fixture >> power_module
            fixture >> switch_module

        # 散热单元
        cooler = Server("液冷源机组")

        # 连接关系
        power_supply >> Edge(label="AC/DC供电") >> main_computer
        power_supply >> Edge(label="模块供电") >> fixture

        main_computer >> Edge(label="GPIB/LAN控制") >> instruments
        main_computer >> Edge(label="CAN/MLVDS通信") >> fixture

        instruments >> Edge(label="测试信号") >> fixture
        cooler >> Edge(label="液冷散热") >> fixture

    print("图表已生成！保存为 'test_system_diagram.png'")

# 执行生成图表
if __name__ == "__main__":
    create_test_system_diagram()