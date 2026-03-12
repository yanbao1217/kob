import { useRecordStore } from "@/stores/record";
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, pk) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.pk = pk;
        this.L = 0;
        // 从后端传来的地图数据获取维度，避免前后端行列数不一致导致渲染失败
        this.map = pk.gp?? [];
        this.rows = this.map.length || 14;
        this.cols = (this.map[0] && this.map[0].length) || 15;
        this.record = useRecordStore()
        if (this.rows === 0 || this.cols === 0) {
            console.error('GameMap: 地图数据为空或格式错误', this.map);
        }

        this.walls = [];
        this.inner_walls_count = 20;

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ]
    }

    create_walls() {
        this.walls = [];
        const g = this.map;
        if (!g || !Array.isArray(g) || g.length === 0) {
            console.error('GameMap: 无法创建墙壁，地图数据无效');
            return;
        }
        for (let r = 0; r < this.rows; r++) {
            const row = g[r];
            if (!row) continue;
            for (let c = 0; c < this.cols; c++) {
                if (row[c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    add_listening_events() {
        let record = this.record
        console.log(this.record)
        if (record.is_record) {
            let k = 0;
            const a_steps = record.a_steps;
            const b_steps = record.b_steps;
            const loser = record.record_loser;
            const [snake0, snake1] = this.snakes;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id)
                } else {
                    snake0.set_direction(parseInt(a_steps[k]))
                    snake1.set_direction(parseInt(b_steps[k]));
                    ++ k;
                }
            }, 300)
        } else {
            this.ctx.canvas.focus();
            
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
                else if (e.key === "ArrowUp") d = 0;
                else if (e.key === "ArrowRight") d = 1;
                else if (e.key === "ArrowDown") d = 2;
                else if (e.key === "ArrowLeft") d = 3;

                if (d >= 0) {
                    this.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }))
                }
            })
        }
    }

    start() {
        this.create_walls();

        this.add_listening_events();
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;

    }

    check_ready() { // 判断两条蛇是否都准备好下一回合了
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false; 
        }
        return true;
    }

    next_step() { // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) { // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) { // 当蛇尾会前进的时候，蛇尾不要判断
                k --;
            }

            for (let i = 0; i < k; i ++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }


        return true;
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r ++) {
            for (let c = 0; c < this.cols; c ++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
} 