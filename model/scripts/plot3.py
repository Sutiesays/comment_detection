from graphviz import Digraph

def create_graphviz_diagram():
    dot = Digraph(comment='自动测试系统硬件结构', format='png')
    dot.attr(rankdir='TB', size='12,8')

    # 定义节点样式
    dot.attr('node', shape='box', style='rounded,filled', color='black')

    # 主控单元
    with dot.subgraph(name='cluster_controller') as c:
        c.attr(label='主控单元', style='filled', color='lightgrey')
        c.node('main', '主控计算机')
        c.node('can', 'CAN接口卡')
        c.node('mlvds', 'MLVDS接口卡')
        c.node('kvm', 'KVM切换器')
        c.edge('main', 'can')
        c.edge('main', 'mlvds')
        c.edge('main', 'kvm')

    # 测试仪器
    with dot.subgraph(name='cluster_instruments') as i:
        i.attr(label='测试仪器集群', style='filled', color='lightyellow')
        i.node('sig', '信号源')
        i.node('spec', '频谱分析仪')
        i.node('osc', '示波器')
        i.node('vna', '矢量网络分析仪')

    # 检测单元
    with dot.subgraph(name='cluster_test') as t:
        t.attr(label='检测单元', style='filled', color='lightcoral')
        t.node('fixture', '专用测试夹具')
        t.node('pwr_mod', '电源模块')
        t.node('sw_mod', '开关矩阵模块')
        t.edge('fixture', 'pwr_mod')
        t.edge('fixture', 'sw_mod')

    # 支撑单元
    dot.node('power', '供电单元\nUPS+交直流电源', shape='box', style='filled,rounded', color='orange')
    dot.node('cooler', '散热单元\n液冷源机组', shape='box', style='filled,rounded', color='cyan')

    # 连接关系
    dot.edge('main', 'sig', label='GPIB/LAN')
    dot.edge('main', 'spec', label='GPIB/LAN')
    dot.edge('main', 'fixture', label='CAN/MLVDS')
    dot.edge('sig', 'fixture', label='射频信号')
    dot.edge('spec', 'fixture', label='测量信号')
    dot.edge('power', 'main', label='AC供电')
    dot.edge('power', 'fixture', label='DC供电')
    dot.edge('cooler', 'fixture', label='液冷散热')

    # 生成图像
    dot.render('test_system_hardware', cleanup=True)
    print("Graphviz图表已生成！保存为 'test_system_hardware.png'")

# 执行生成
if __name__ == "__main__":
    create_graphviz_diagram()